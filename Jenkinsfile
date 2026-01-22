pipeline {
	
	agent none
	
	stages {
		stage('Build image'){
			agent {
				kubernetes {
					label 'docker-agent'
					yaml """
apiVersion: v1
kind: Pod
metadata:
  name: docker-building
  namespace: k3s1on10
spec:
  containers:
  - name: building-image
    image: docker:latest
    command:
    - cat
    tty: true	
    volumeMounts:
    - name: docker-socket
      mountPath: /var/run/docker.sock
    - name: docker-conf
      mountPath: /tmp/.docker
  volumes:
    - name: docker-socket
      hostPath:
        path: /var/run/docker.sock
    - name: docker-conf
      secret:
        secretName: docker-hub-secret
        items:
        - key: .dockerconfigjson
          path: config.json	
"""
				}
			}
			steps {
				checkout scm
				container('building-image') {
					script {
						sh '''
						mkdir -p /root/.docker
						cp /tmp/.docker/config.json /root/.docker/config.json
						docker build -t kyul1234/k3s1on10:latest .
						docker push kyul1234/k3s1on10:latest
						'''
					}
				}
				
				
			}
		}
		stage('Deploy to a pod') {
			agent {
				kubernetes {
					label 'docker-agent'
					yaml """
apiVersion: v1
kind: Pod
metadata:
  name: deployment
  namespace: k3s1on10
spec:
  serviceAccountName: k3s1on10
  containers:
  - name: deployer
    image: alpine/k8s:1.32.11
    command:
    - cat
    tty: true
"""
				}
			}
			steps {
				checkout scm
				container('deployer') {
					sh '''
					kubectl apply -f deployment.yaml
					'''
				}
			}
		}
		
	}
	
}