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
  name: building
  namespace: k3s1on10
spec:
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    command:
    - cat
    tty: true	
    volumeMounts:
    - name: docker-conf
      mountPath: /kaniko/.docker
  volumes:
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
				container('kaniko') {
					script {
						sh '''
						/kaniko/executor \
						--context \$(pwd) \
						--dockerfile \$(pwd)/Dockerfile \
						--destination kyul1234/k3s1on10:latest
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
					kubectl apply -f app-config.yaml
					kubectl apply -f deployment.yaml
					'''
				}
			}
		}
		
	}
	
}