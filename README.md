

## Project Quick Links

- **Code Repository:**  
  [https://github.com/ujjwalnaggaro01/project-management-demo](https://github.com/ujjwalnaggaro01/project-management-demo)

- **Docker Images:**  
  Available at Docker Hub:  
  [ujjwalraghuvanshi1212/project-service](https://hub.docker.com/repository/docker/ujjwalraghuvanshi1212/project-service/tags/026fff9/sha256-a71dede364bd9e7ec24ac154a48c23fb90c75c6f10e1fd0960e9d96b929cce01)

- **Service API Endpoint:**  
  URL to access backend data and view records (replace `<EXTERNAL-IP>` with your Ingress IP or domain):  
  `http://<EXTERNAL-IP>/api/v1/projects/pm/2`
  
  Example used in demo:  
  [http://34.47.247.229/api/v1/projects/pm/2](http://34.47.247.229/api/v1/projects/pm/2)

- **Docker Images:**  
  Recording:  
  [Demo](https://nagarro-my.sharepoint.com/:v:/p/ujjwal01/Ef1JQgmeDcdBuyc-AE9eG74BC_FAj6Y8nQL7WQeKBPn7NA?e=qjqMmV)



## Run with Docker

You can run the Project Management Service and a MySQL database using Docker containers for easy local development/testing.

### 1. Build the Docker image
If you haven't already built the image:
```sh
docker build -t project-service:latest .
```

### 2. Run MySQL with Docker
```sh
docker run --name mysql-local -e MYSQL_ROOT_PASSWORD=yourpassword -e MYSQL_DATABASE=projectmanagement -p 3306:3306 -d mysql:8.0
```

### 3. Run the application container
```sh
docker run --name project-service --link mysql-local:mysql -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/projectmanagement -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=yourpassword -p 8080:8080 project-service:latest
```

> **Note:**
> - Adjust environment variables as needed for your setup.
> - The `--link` flag is for simple local testing. For production or advanced local setups, use Docker Compose or Kubernetes.

---

## Local Setup Instructions

To run and develop this project locally, follow these steps:

1. **Clone the repository:**
   ```sh
   git clone https://github.com/ujjwalnaggaro01/project-management-demo.git
   cd project-management-demo
   ```

2. **Build the project (requires Java 17+ and Maven):**
   ```sh
   mvn clean install
   ```


3. **Configure the database:**
   - By default, the project uses MySQL. You can install and manage MySQL locally using [MySQL Workbench](https://dev.mysql.com/downloads/workbench/).
   - Download and install MySQL Workbench and MySQL Server from the official site: [https://dev.mysql.com/downloads/](https://dev.mysql.com/downloads/)
   - Create a database named `projectmanagement` and a user with appropriate privileges.
   - Update `src/main/resources/application.yml` with your local DB credentials if needed.

4. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```
   The service will start on [http://localhost:8080](http://localhost:8080) by default.

5. **API Documentation:**
   - You can enable Swagger/OpenAPI if needed and, visit [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for API docs.

6. **Run tests:**
   ```sh
   mvn test
   ```

---

<h1>Project Management Service - Kubernetes Deployment Guide</h1>


<p>This guide provides step-by-step instructions to deploy and manage the Project Management Service on a Kubernetes cluster. Each step is explained for clarity. You can copy-paste the commands as needed.</p>

<hr/>

<h2>1. Build and Push Docker Image (Windows)</h2>
<p>Build a new Docker image after code changes, tag it, and push to Docker Hub. Replace <code>$GIT_TAG</code> with your git commit hash:</p>
<pre><code>Double click on the docker-build.bat inside the folder
</code></pre>
<hr/>

<h2>2. Install Ingress Controller</h2>
<p>Apply the official NGINX ingress controller manifest to your cluster:</p>
<pre><code>kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.11.2/deploy/static/provider/cloud/deploy.yaml
</br>
kubectl patch svc ingress-nginx-controller -n ingress-nginx --type='json' -p='[{"op": "replace", "path": "/spec/externalTrafficPolicy", "value": "Cluster"}]'
</code></pre>
<hr/>

<h2>3. Create Namespace</h2>
<p>Create a dedicated namespace for the project resources:</p>
<pre><code>kubectl apply -f infra/app/namespace.yaml
</code></pre>
<hr/>

<h2>4. Deploy Database Resources</h2>
<p>Apply the following manifests to set up the database, secrets, configuration, storage class, and stateful set:</p>
<pre><code>kubectl apply -f infra/db/db-secret.yaml
kubectl apply -f infra/db/db-config.yaml
kubectl apply -f infra/db/db-storageclass.yaml
kubectl apply -f infra/db/db-statefulset.yaml
</code></pre>
<hr/>

<h2>5. Run Database Migration Job</h2>
<p>Apply migration configs and run the migration job to set up the initial schema and data:</p>
<pre><code>kubectl apply -f infra/db/db-migration-config.yaml
kubectl apply -f infra/db/db-migration-job.yaml
kubectl logs job/flyway-migration -n projectmanagement
</code></pre>
<hr/>

<h2>6. Deploy Application Resources</h2>
<p>Apply secrets, config, deployment, and ingress for the application:</p>
<pre><code>kubectl apply -f infra/app/app-secret.yaml
kubectl apply -f infra/app/app-config.yaml
kubectl apply -f infra/app/app-deployment.yaml
kubectl apply -f infra/app/app-ingress.yaml

kubectl set image deployment/project-service project-service=ujjwalraghuvanshi1212/project-service:026fff9 -n projectmanagement
</code></pre>
<hr/>

<h2>7. Manage Stateful MySQL Pods</h2>
<ul>
  <li><b>Restart MySQL StatefulSet:</b></li>
</ul>
<pre><code>kubectl rollout restart statefulset mysql -n projectmanagement
kubectl rollout status statefulset mysql -n projectmanagement
</code></pre>
<ul>
  <li><b>Delete a MySQL Pod (if needed):</b></li>
</ul>
<pre><code>kubectl get pods -n projectmanagement
kubectl delete pod &lt;pod_name&gt; -n projectmanagement
</code></pre>
<hr/>

<h2>8. Manage Application Deployment</h2>
<ul>
  <li><b>Restart the application deployment:</b></li>
</ul>
<pre><code>kubectl rollout restart deployment project-service -n projectmanagement
kubectl rollout status deployment project-service -n projectmanagement
</code></pre>
<hr/>

<h2>9. Delete All Resources</h2>
<p>To clean up all resources in the <code>projectmanagement</code> namespace:</p>
<pre><code>kubectl delete all,configmaps,pvc,secrets,serviceaccounts,pv,storageclass --all -n projectmanagement
</code></pre>
<hr/>