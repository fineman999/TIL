# Get latest AMI ID for Amazon Linux2 OS
data "aws_ami" "amz-linux2" {
  most_recent = true # Get the latest AMI
  owners = ["amazon"] # Get the AMI from Amazon
  filter {
    name = "name"
    values = ["amzn2-ami-kernel-5.10-hvm-*-gp2"] # Amazon Linux 2 AMI
  }
  filter {
    name = "root-device-type"
    values = ["ebs"]
  }
  filter {
    name = "virtualization-type"
    values = ["hvm"]
  }
  filter {
    name = "architecture"
    values = ["x86_64"]
  }
}
