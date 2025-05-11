# Terraform Block
/**
터미널에 현재 위치한 디렉토리에서 terraform 명령어를 실행할 때 사용할 Terraform 버전을 지정합니다.
terraform version
Terraform v1.10.5
on darwin_arm64
+ provider registry.terraform.io/hashicorp/aws v5.86.0
 */
terraform {
  required_version = "~> 1.10.5" # 1.10.x 버전만 허용 (1.11.0 이상은 불가)
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.87" # 5.87.x 버전만 허용 (5.88.0 이상은 불가)
    }
  }
}
# Provider Block
provider "aws" {
  region  = var.aws_region
  profile = "default" # AWS Credentials Profile configured on your local desktop terminal  $HOME/.aws/credentials
}

/**
Note-1: AWS Credentials Profile configured on your local desktop terminal  $HOME/.aws/credentials
Note-2: Seoul Region
 */

