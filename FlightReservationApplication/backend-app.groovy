pipeline {
    agent any 
    stages {
        stage('Code-Pull'){
            steps{
                git branch: 'main', url: 'https://github.com/prasad3591/flight-reservation-app.git'    
            }
        }
        stage('test'){
            steps{
               echo 'testing...'
            }
        }
        stage('deploying'){
            steps{
               echo 'deploying...'
            }
        }
    }
}