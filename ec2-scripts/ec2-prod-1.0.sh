#!/usr/bin/bash

# Install packages
yum -y update
amazon-linux-extras install -y java-openjdk11
amazon-linux-extras install -y nginx1
yum install -y java-11-openjdk-devel git
su ec2-user -l -c 'curl -s "https://get.sdkman.io" | bash && source .bashrc && sdk install gradle'

# Configure/install custom software
cd /home/ec2-user
git clone https://github.com/speakingrooman/java-image-gallery.git
chown -R ec2-user:ec2-user java-image-gallery

echo "problem is in ec2 scipts"
CONFIG_BUCKET="s3://edu.au.cc.image-gallery-mohammad-config"
sudo aws s3 cp ${CONFIG_BUCKET}/nginx/nginx.conf /etc/nginx/nginx.conf
sudo aws s3 cp ${CONFIG_BUCKET}/nginx/default.d/image_gallery.conf /etc/nginx/default.d/image_gallery.conf





# Start/enable services
systemctl stop postfix
systemctl disable postfix
systemctl start nginx
systemctl enable nginx


sudo aws s3 cp s3://edu.au.cc.image-gallery-mohammad-config/nginx/nginx.conf /etc/nginx/nginx.conf
sudo aws s3 cp s3://edu.au.cc.image-gallery-mohammad-config/nginx/default.d/image_gallery.conf /etc/nginx/default.d/image_gallery.conf




su ec2-user -l -c 'cd ~/java-image-gallery && ./start' >/var/log/image_gallery.log 2>&1 &
