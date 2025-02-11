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


