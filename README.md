kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.11.2/deploy/static/provider/cloud/deploy.yaml
kubectl patch svc ingress-nginx-controller -n ingress-nginx --type='json' -p='[{"op": "replace", "path": "/spec/externalTrafficPolicy", "value": "Cluster"}]'

kubectl apply -f infra/app/namespace.yaml
kubectl apply -f infra/db/db-secret.yaml
kubectl apply -f infra/db/db-config.yaml
kubectl apply -f infra/db/db-storageclass.yaml
kubectl apply -f infra/db/db-statefulset.yaml

kubectl exec -it mysql-0 -n projectmanagement -- /bin/bash
mysql -u root -p ggH{W1qyu9oywe!r

kubectl apply -f infra/db/db-migration-config.yaml 
kubectl apply -f infra/db/db-migration-job.yaml
kubectl logs job/flyway-migration -n projectmanagement



kubectl apply -f infra/app/app-secret.yaml
kubectl apply -f infra/app/app-config.yaml
kubectl apply -f infra/app/app-deployment.yaml
kubectl apply -f infra/app/app-ingress.yaml

Testing Deletion:
kubectl rollout restart statefulset mysql -n projectmanagement
kubectl get pods -n projectmanagement







pvc-8e97697f-79a5-4fbf-a94e-ce5df5d0a9fa   1Gi        RWO            Delete           Bound    projectmanagement/mysql-data-mysql-0   pd-standard    <unset>                          20m
 kubectl delete all,configmaps,pvc,secrets,serviceaccounts,pv,storageclass --all -n projectmanagemen