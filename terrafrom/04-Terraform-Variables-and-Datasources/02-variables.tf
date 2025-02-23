# Input Variables
# AWS Region
variable "aws_region" {
  description = "Region in which AWS Resources to be created"
  type        = string
  default     = "ap-northeast-2"
}

# AWS EC2 Instance Type
variable "instance_type" {
  description = "EC2 Instance Type"
  type        = string
  default     = "t2.micro"
  validation {
    condition     = length(var.instance_type) > 0
    error_message = "Instance Type cannot be empty"
  }
}


# AWS EC2 Instance Key Pair
variable "instance_keypair" {
  description = "AWS EC2 Key Pair that allows SSH access to the instance"
  type        = string
  default     = "weplat-ap2-key"
}