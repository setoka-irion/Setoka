packer {
    required_plugins {
        amazon = {
        source = "github.com/hashicorp/amazon"
        version = ">= 1.0.0"
        }
    }
}
variable "aws_region" {
    type = string
    default = "ap-northeast-2"
}
variable "source_ami" {
    type = string
    default = "ami-059fffe0a503646a6"
}
variable "instance_type" {
    type = string
    default = "t3.micro"
}
variable "s3_bucket" {
    type = string
}
variable "db_url" {
    type = string
}
variable "db_username" {
    type = string
}
variable "db_password" {
    type = string
}
variable "upload_path" {
    type = string
}
variable "efs_id" {
    type = string
}
source "amazon-ebs" "irion_ami" {
    region = var.aws_region
    source_ami = var.source_ami
    instance_type = var.instance_type
    ssh_username = "ubuntu"
    ami_name = "irion-ami-{{timestamp}}"
    associate_public_ip_address = true
    iam_instance_profile = "irionRole"

    tags = {
        Name = "irion-packer-ami"
    }
}
build {
    sources = ["source.amazon-ebs.irion_ami"]
    provisioner "shell" {
        inline = [
            "sudo apt update",
            "sudo apt install -y openjdk-17-jdk unzip curl net-tools nfs-common",
            "sudo mount -t nfs4 -o nfsvers=4.1,rsize=1048576,wsize=1048576,hard,timeo=600,retrans=2,noresvport ${var.efs_id}:/ /mnt/efs/uploads/",
            "sudo chmod 777 /mnt/efs/uploads",
            "sudo chown ubuntu:ubuntu /mnt/efs/uploads",
            "sudo mkdir -p /home/ubuntu/irion",
            "sudo chown -R ubuntu:ubuntu /home/ubuntu/irion",
            "sudo aws s3 cp s3://${var.s3_bucket}/IRI-ON.jar /home/ubuntu/irion/IRI-ON.jar",
            "sudo chown ubuntu:ubuntu /home/ubuntu/irion/IRI-ON.jar",
            "echo '[Unit]' | sudo tee /etc/systemd/system/IRI-on.service",
            "echo 'Description=Spring Boot App' | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo 'After=network.target' | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo '[Service]' | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo 'User=ubuntu' | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo 'WorkingDirectory=/home/ubuntu/irion' | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo \"Environment=DB_URL=${var.db_url}\" | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo \"Environment=DB_USERNAME=${var.db_username}\" | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo \"Environment=DB_PASSWORD=${var.db_password}\" | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo \"Environment=UPLOAD_PATH=${var.upload_path}\" | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo 'ExecStart=/usr/bin/java -jar /home/ubuntu/irion/IRI-ON.jar' | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo 'Restart=always' | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo 'RestartSec=5' | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo '[Install]' | sudo tee -a /etc/systemd/system/IRI-on.service",
            "echo 'WantedBy=multi-user.target' | sudo tee -a /etc/systemd/system/IRI-on.service",
            "sudo systemctl daemon-reload",
            "sudo systemctl enable IRI-on"
        ]
    }
}