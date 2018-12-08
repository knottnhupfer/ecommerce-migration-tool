pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
        }
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -f app/pom.xml -B -DskipTests clean install' 
            }
        }
    }
}
