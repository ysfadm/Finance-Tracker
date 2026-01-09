# Finance Tracker - Deployment Guide

## Deployment Options

This guide covers multiple deployment options for the Finance Tracker application.

---

## 1. Docker Compose (Recommended for Development/Testing)

### Prerequisites

- Docker 20.10+
- Docker Compose 2.0+

### Steps

1. **Prepare Environment**

```bash
cd Finance-Tracker
cp .env.example .env
```

2. **Update Configuration**

Edit `.env` with your settings:

```env
DB_PASSWORD=secure_password_here
JWT_SECRET=very_long_secret_key_minimum_32_chars
```

3. **Start Services**

```bash
docker-compose up -d
```

4. **Verify Deployment**

```bash
# Check services
docker-compose ps

# Check logs
docker-compose logs -f backend

# Test API
curl http://localhost:8080/api/auth/validate

# Access pgAdmin
# Visit: http://localhost:5050
# Email: admin@example.com (from .env)
```

5. **Stop Services**

```bash
docker-compose down
```

---

## 2. Kubernetes Deployment

### Prerequisites

- Kubernetes cluster (1.20+)
- kubectl configured
- Helm 3.0+ (optional)

### Manual Kubernetes Setup

1. **Create Namespace**

```bash
kubectl create namespace finance-tracker
```

2. **Create Secrets**

```bash
kubectl create secret generic db-credentials \
  --from-literal=username=postgres \
  --from-literal=password=secure_password \
  -n finance-tracker

kubectl create secret generic jwt-secret \
  --from-literal=secret=your_jwt_secret_key \
  -n finance-tracker
```

3. **Deploy PostgreSQL**

```bash
kubectl apply -f k8s/postgres-deployment.yaml -n finance-tracker
kubectl apply -f k8s/postgres-service.yaml -n finance-tracker
```

4. **Deploy Backend**

```bash
kubectl apply -f k8s/backend-deployment.yaml -n finance-tracker
kubectl apply -f k8s/backend-service.yaml -n finance-tracker
```

5. **Verify Deployment**

```bash
kubectl get pods -n finance-tracker
kubectl get services -n finance-tracker
kubectl logs -f deployment/finance-tracker-api -n finance-tracker
```

---

## 3. AWS ECS Deployment

### Prerequisites

- AWS Account
- AWS CLI configured
- ECR repository created

### Steps

1. **Build and Push Docker Image**

```bash
# Login to ECR
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

# Build image
docker build -f backend/Dockerfile -t finance-tracker-api:latest .

# Tag image
docker tag finance-tracker-api:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/finance-tracker-api:latest

# Push image
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/finance-tracker-api:latest
```

2. **Create RDS PostgreSQL Database**

```bash
aws rds create-db-instance \
  --db-instance-identifier finance-tracker-db \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --master-username postgres \
  --master-user-password <secure-password> \
  --allocated-storage 20
```

3. **Create ECS Cluster**

```bash
aws ecs create-cluster --cluster-name finance-tracker
```

4. **Register Task Definition**

Update `ecs/task-definition.json` with your ECR image URL, then:

```bash
aws ecs register-task-definition \
  --cli-input-json file://ecs/task-definition.json
```

5. **Create Service**

```bash
aws ecs create-service \
  --cluster finance-tracker \
  --service-name finance-tracker-api \
  --task-definition finance-tracker-api:1 \
  --desired-count 2 \
  --launch-type EC2
```

---

## 4. Heroku Deployment

### Prerequisites

- Heroku account
- Heroku CLI installed
- Git repository initialized

### Steps

1. **Create Heroku App**

```bash
heroku create finance-tracker-api
```

2. **Add PostgreSQL Add-on**

```bash
heroku addons:create heroku-postgresql:hobby-dev
```

3. **Set Environment Variables**

```bash
heroku config:set \
  JWT_SECRET=your_secret_key \
  LOG_LEVEL=INFO
```

4. **Deploy**

```bash
git push heroku main
```

5. **Initialize Database**

```bash
heroku run "java -jar app.jar --spring.jpa.hibernate.ddl-auto=create"
```

---

## 5. DigitalOcean App Platform

### Steps

1. **Connect GitHub Repository**

- Visit: https://cloud.digitalocean.com/apps
- Click "Create App"
- Select your GitHub repository

2. **Configure App**

```yaml
name: finance-tracker-api
services:
  - name: backend
    github:
      repo: your-username/finance-tracker
      branch: main
    build_command: mvn clean package -DskipTests
    run_command: java -jar backend/target/finance-tracker-api-*.jar
    envs:
      - key: DB_URL
        scope: RUN_TIME
        value: ${db.connection_string}
databases:
  - name: db
    engine: PG
    version: "15"
```

---

## 6. Self-Hosted (VPS/Dedicated Server)

### Prerequisites

- Ubuntu 22.04 LTS
- Sudo access
- Domain name (optional)

### Installation Steps

1. **System Setup**

```bash
sudo apt update
sudo apt upgrade -y

# Install Java
sudo apt install -y openjdk-21-jdk

# Install PostgreSQL
sudo apt install -y postgresql postgresql-contrib

# Install Docker (optional)
sudo apt install -y docker.io docker-compose
```

2. **Create Application User**

```bash
sudo useradd -m -s /bin/bash finance-tracker
sudo usermod -aG sudo finance-tracker
```

3. **Setup PostgreSQL**

```bash
sudo -u postgres createdb finance_tracker_db
sudo -u postgres createuser finance_tracker
```

4. **Deploy Application**

```bash
sudo -u finance-tracker mkdir -p /opt/finance-tracker
sudo cp backend/target/finance-tracker-api-*.jar /opt/finance-tracker/app.jar

# Create systemd service
sudo tee /etc/systemd/system/finance-tracker.service > /dev/null <<EOF
[Unit]
Description=Finance Tracker API
After=network.target postgresql.service

[Service]
Type=simple
User=finance-tracker
WorkingDirectory=/opt/finance-tracker
ExecStart=/usr/bin/java -jar app.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF
```

5. **Start Service**

```bash
sudo systemctl daemon-reload
sudo systemctl enable finance-tracker
sudo systemctl start finance-tracker
```

6. **Setup Nginx (Reverse Proxy)**

```bash
sudo apt install -y nginx

# Configure nginx
sudo tee /etc/nginx/sites-available/finance-tracker > /dev/null <<EOF
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF

sudo ln -s /etc/nginx/sites-available/finance-tracker /etc/nginx/sites-enabled/
sudo systemctl restart nginx
```

---

## Monitoring & Maintenance

### Health Checks

```bash
# API Health
curl http://localhost:8080/api/auth/validate

# Database Connection
curl http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer <TOKEN>"
```

### Log Management

```bash
# Docker
docker-compose logs -f backend

# Kubernetes
kubectl logs -f deployment/finance-tracker-api

# System Service
sudo journalctl -u finance-tracker -f
```

### Database Backup

```bash
# PostgreSQL Dump
pg_dump -U postgres finance_tracker_db > backup.sql

# Automated Backup
0 2 * * * pg_dump -U postgres finance_tracker_db | gzip > /backups/db-$(date +\%Y\%m\%d).sql.gz
```

### Performance Monitoring

Monitor these metrics:

- CPU Usage
- Memory Usage
- Database Query Time
- API Response Time
- Request Count

---

## Troubleshooting

### Connection Refused

```bash
# Check if service is running
docker-compose ps
kubectl get pods
systemctl status finance-tracker

# Check port
lsof -i :8080
```

### Database Issues

```bash
# Check PostgreSQL
psql -U postgres -d finance_tracker_db

# View logs
docker-compose logs postgres
```

### Out of Memory

```bash
# Increase Java heap
export JAVA_OPTS="-Xms512m -Xmx1g"
```

---

## Security Checklist

- [ ] Change default JWT secret
- [ ] Use strong database password
- [ ] Enable HTTPS/SSL
- [ ] Set up firewall rules
- [ ] Enable database backups
- [ ] Configure log rotation
- [ ] Set up monitoring alerts
- [ ] Regularly update dependencies
- [ ] Use environment-specific configs
- [ ] Review security headers

---

## Rollback Procedure

```bash
# Docker
docker-compose down
docker-compose up -d

# Kubernetes
kubectl rollout undo deployment/finance-tracker-api

# Manual
systemctl stop finance-tracker
# Replace jar file
systemctl start finance-tracker
```

---

**Last Updated:** January 9, 2026
