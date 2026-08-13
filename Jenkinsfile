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
            bat 'mvn clean test-compile'

            bat '''
                echo ===== CHECK RUNNER SOURCE =====
                dir src\\test\\java\\com\\tutorialsninja\\runners

                echo ===== CHECK RUNNER CLASS =====
                dir target\\test-classes\\com\\tutorialsninja\\runners
            '''

            bat 'mvn test -Dtest=SmokeTestRunner'
        }
    }
}