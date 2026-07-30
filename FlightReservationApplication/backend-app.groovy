pipeline {
    agent any 
    stages {
        stage('Code-Pull'){
            steps{
                git branch: 'main', url: 'https://github.com/prasad3591/flight-reservation-app.git'    
            }
        }
        stage('Buid'){
            steps{
               sh ''' 
                 cd FlightReservationApplication
                 mvn clean package
                '''
            }
        }
        stage('Debug') {
            steps {
                 sh '''
                     whoami
                     pwd
                     java -version
                     mvn -version
                '''
                }
        }
        stage('QA-Test') {
        steps {
            withSonarQubeEnv('sonar') {
            sh '''
                cd FlightReservationApplication
                mvn clean verify sonar:sonar \
                  -Dsonar.projectKey=flight-reservation-backend
            '''
            }
          }
        }
        stage('Docker'){
            steps{
                sh ''' 
                 cd FlightReservationApplication
                 docker build -t prasad3591/flight-reservation-app-pls-19-20:latest .
                 docker push prasad3591/flight-reservation-app-pls-19-20:latest
                 docker rmi prasad3591/flight-reservation-app-pls-19-20:latest
                '''   
            }
        }
        stage('Deploy'){
            steps{
                sh ''' 
                 cd FlightReservationApplication
                 kubectl apply -f k8s/*
                '''  
            }
        }
    }
}