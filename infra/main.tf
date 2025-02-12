terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "6.20.0"
  }
 }
}

# Configure the GCP provider
provider "google" {
  project = "retro-450604"
  region  = "asia-south1"
}

# Create an Artifact Registry repository for Docker images
resource "google_artifact_registry_repository" "docker_repo" {
  repository_id = "docker-repo"      # Unique identifier for repository
  location      = "asia-south1"      
  format        = "DOCKER"           # Specifies that this repo is for Docker images
  description   = "Repository for Docker images"
}

# Create the Cloud Run Service
resource "google_cloud_run_service" "default" {
  name     = "retro-backend-service"
  location = "asia-south1"

  template {
    spec {
      containers {
        image = "asia-south1-docker.pkg.dev/retro-450604/docker-repo/retro-backend:2151b91dde7c39054c1ea7cf6e547684d6d6c08d"
        
        ports {
          container_port = 8081
        }
      }
    }
  }

  # Direct 100% of the traffic to the latest revision
  traffic {
    percent         = 100
    latest_revision = true
  }
}

# Allow unauthenticated invocations
resource "google_cloud_run_service_iam_member" "noauth" {
  service  = google_cloud_run_service.default.name
  location = google_cloud_run_service.default.location
  role     = "roles/run.invoker"
  member   = "allUsers"
}


