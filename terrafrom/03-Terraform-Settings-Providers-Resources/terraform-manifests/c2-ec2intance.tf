# Resource: EC2 Instance
resource "aws_instance" "ec2demo" {
  ami           = "ami-0cee4e6a7532bb297" # Amazon Linux 2 AMI
  instance_type = "t2.micro"
  user_data = file("${path.module}/app1-install.sh")
  tags = {
    "Name" = "EC2-Demo"
  }
}