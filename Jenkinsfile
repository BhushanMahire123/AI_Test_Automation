pipeline {

    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Clean') {
            steps {
                bat 'mvn clean'
            }
        }

        stage('Run Smoke Test') {
            steps {
                bat 'mvn test -Dtest=SmokeTestRunner'
            }
        }
    }

    post {

        always {
            echo 'Automation execution completed.'
        }

        success {
            echo 'Smoke Test PASSED - @TC_LOGIN_002'
        }

        failure {
            echo 'Smoke Test FAILED - @TC_LOGIN_002'
        }
    }
}