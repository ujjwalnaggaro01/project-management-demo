

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