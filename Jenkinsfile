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

            junit allowEmptyResults: true,
                  testResults: 'target/surefire-reports/*.xml'

            publishHTML([
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/cucumber-reports',
                reportFiles: 'report.html',
                reportName: 'Cucumber HTML Report'
            ])
        }

        success {
            echo 'Smoke Test PASSED - @TC_LOGIN_002'
        }

        failure {
            echo 'Smoke Test FAILED - @TC_LOGIN_002'
        }
    }
}