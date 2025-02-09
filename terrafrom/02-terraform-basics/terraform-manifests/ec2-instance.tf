# Terraform Settings Block
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      #version = "~> 3.21" # Optional but recommended in production
    }
  }
}

# Provider Block
provider "aws" {
  profile = "default" # AWS Credentials Profile configured on your local desktop terminal  $HOME/.aws/credentials
  region  = "ap-northeast-2" # Seoul Region
}

# Resource Block
resource "aws_instance" "ec2demo" {
  ami           = "ami-0cee4e6a7532bb297" # Amazon Linux 2 AMI
  instance_type = "t2.micro"
}