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
  default     = "terraform-key.pem"
}
# AWS EC2 Instance Type - List
variable "instance_type_list" {
  description = "EC2 Instance type list"
  type = list(string)
  default = ["t2.micro", "t2.small"]
}

# AWS EC2 Instance Type - Map
variable "instance_type_map" {
  description = "EC2 Instance Type Map"
  type = map(string)
  default = {
    "dev": "t2.micro"
    "qa": "t2.small"
    "prod":"t2.large"
  }
}