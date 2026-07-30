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
        stage('QA-Test') {
            steps {
            withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-token') {
            sh '''
                cd FlightReservationApplication
                mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
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