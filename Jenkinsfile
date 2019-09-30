pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    options { buildDiscarder(logRotator(daysToKeepStr: '5', numToKeepStr: '5')) }
    environment {
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
        TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
    }

    // ###################################################################################################################
    // Starting Pipeline Stages
    // ###################################################################################################################
    
	stages {
		stage('Cleaning Reports Folder') {
            	steps {
					script {
                    dir('reports') {
					deleteDir()
					}
                }
            }
	    }
        stage('Setting Environment') {
            steps {
                echo "Starting Build and Deployment Process. Setting up environment vars"

                // Read from environment specific config and set Environment variables
                script {

                    if (env.BRANCH_NAME == 'develop') {
                        echo 'Triggering build from develop branch'
                        env.ENV = "dev"
                    } else if (env.BRANCH_NAME == 'qa') {
                        echo 'Triggering build from qa branch'
                        env.ENV = "qa"
                    } else if (env.BRANCH_NAME == 'master') {
                        echo 'Triggering build from qa branch'
                        env.ENV = "prod"
                    } else {
                        echo 'Not triggering the build. Only supported from [develop | qa | master] branches'
                    }

                    echo "Build Environment : ${ENV}"
                    pom = readMavenPom file: "./pom.xml"
                    env.ARTIFACT_VERSION = pom.version
                    env.ARTIFACTID = pom.artifactId
                    env.GROUPID = pom.groupId
                    env.GROUPID_PATH = GROUPID.replaceAll('\\.', '/');
                    if (pom.version.contains('SNAPSHOT')) {
                        env.REPOTYPE = "maven-snapshots"
                    } else {
                        env.REPOTYPE = "maven-releases"
                    }
                    env.BUILD_DIR = "./target"
                    env.REPO_URL = "${BASE_REPO_URL}"
                    env.REGION_NAME = "${AWS_REGION}"
                    env.DOMAIN_NAME = "${AWS_DOMAIN_NAME}"
                    env.OC_PROJECT_QA = "jetsqa"
                    
                }
            }
        }
	// ###################################################################################################################
        // Create Artifact
    // ###################################################################################################################

        stage('Execute Artifact') {
            steps {
                script {
                    sh "sed -i 's/OC_PROJECT/${OC_PROJECT_QA}/g' URLs.txt"
                    sh "sed -i 's/DOMAIN_NAME/${DOMAIN_NAME}/g' URLs.txt"
    		        sh "mkdir -p ${BUILD_DIR}"
                    sh "mvn clean install -U -B -DskipTests=true -Dproject.build.directory=${BUILD_DIR}"
                    }
                }
        }

        // ###################################################################################################################
        // Build Docker Image and publish to Registry
        // ###################################################################################################################

        stage('Build, validate and Publish Docker Image') {
            agent none

           steps {
             script {
                  echo "Building Docker Image"

                    sh "docker build -t ${REPO_URL}/jets-security-automation:latest --build-arg APP_ENV=${ENV} --build-arg SERVICE_PORT=8090 --build-arg APP_NAME=jets-security-framework --build-arg DEPLOY_ARTIFACT_DIR=target --build-arg ARTIFACT_NAME=jets-bio-test-security-0.0.1-SNAPSHOT.jar --no-cache . --rm=true"
                    // Create Repo, if it does not exist
                    script {
                        echo "Creating Repository, if doesn't exist"
                        
                         try {
                            sh "eval \$(aws ecr get-login --no-include-email --region ${REGION_NAME})"
                            sh "aws ecr describe-repositories --region ${REGION_NAME} --repository-names jets-security-automation 2>&1 > /dev/null"
                        } catch(Exception e) {
                            echo "Repository does not exist. Creating ..."
                            sh  "aws ecr create-repository --region ${REGION_NAME} --repository-name jets-security-automation"
                        }
                    
                    	sh "docker push ${REPO_URL}/jets-security-automation:latest"
                    	echo "Docker image is pushed to ECR"
                }
           }
        }
    }

        stage('Execute Automation Tests') {
        agent {
			    label 'security-automation'
            }
            steps {
                script {
                    sh "sed -i 's/OC_PROJECT/${OC_PROJECT_QA}/g' URLs.txt"
                    sh "sed -i 's/DOMAIN_NAME/${DOMAIN_NAME}/g' URLs.txt"
                    sh "java -Xmx512m -jar ./ZAP/zap-2.8.0.jar -daemon -host localhost -port 8090 -dir tmp -config scanner.threadPerHost=20 -config spider.thread=10 -config  api.disablekey=true &"
                    sh "mvn clean test -DZAP_PROXYHOST=localhost -DZAP_PROXYPORT=8090 -DUSE_ZAP_DEAMON=true -DBROWSER=chrome -DZAP_OS=linux -DBROWSER_DRIVER_VERSION=74.0.3729.6"
                    stash includes: "**/reports/**", useDefaultExcludes: true, excludes:"**/.git, **/.git/**", name:"${ARTIFACTID}-${BUILD_NUMBER}"
                    }
                }
            }
        
        stage('Unstash Test Report') {
            steps {
                script {
			unstash "${ARTIFACTID}-${BUILD_NUMBER}"
                }
            }
        }

    }

    post {
             success {
                    echo 'One way or another, I have finished'
                    archiveArtifacts 'reports/**/*.html'          
    		        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'reports', reportFiles: 'report.html', reportName: 'Security Report', reportTitles: 'Security Report'])
                }
                failure {
                    echo 'Failed to generate test report'
                    archiveArtifacts 'reports/**/*.html'          
    		        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'reports', reportFiles: 'report.html', reportName: 'Security Report', reportTitles: 'Security Report'])
                
                }
            }
        }
