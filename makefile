SOURCE_DIR := "./aws/${STACK}/cf"
CONFIG_DIR := "./build-config/${ENV}/${STACK}"
DIST_DIR := "./dist-${STACK}"
TAR_FILE := "${STACK}-${BUILD_VERSION}.tgz"

# Validate Environment var is set
# guard-%:
# 	@echo "--> Confirming environment variable ${*} is set"
# 		@if [ "${${*}}" = ""]; then \
# 				echo "!! Environment variable $* not set !!"; \
# 						exit 1; \
# 							fi
# 								
# 								# Make the dist folder
# 								make-dist-dir: guard-DIST_DIR
# 									rm -rf ${DIST_DIR}
# 										mkdir ${DIST_DIR}
#
# 										# Package the template using the Salt configuration
# 										package-stack: guard-BUILD_IMAGE_PATH guard-BUILD_IMAGE_VERSION make-dist-dir
# 											@echo "Packaging for Stack : ${STACK} for Environment: ${ENV}"
# 												pwd
# 													@echo "Source Dir : ${SOURCE_DIR}, Config Directory: ${CONFIG_DIR}"
# 														cp -R ${SOURCE_DIR}/* ${DIST_DIR}/
# 															cp -R ${CONFIG_DIR}/* ${DIST_DIR}/
# 																ls -lrt ${DIST_DIR}
# 																	sed -i -e "s|@@BUILD_IMAGE_PATH@@|${BUILD_IMAGE_PATH}|g" ${DIST_DIR}/cf.config
# 																		sed -i -e "s|@@BUILD_IMAGE_VERSION@@|${BUILD_IMAGE_VERSION}|g" ${DIST_DIR}/cf.config
# 																			echo "parameter.forceUpdate=`date`" >> ${DIST_DIR}/cf.config
# 																				tar -zcvf ${TAR_FILE} -C ${DIST_DIR} .
# 																					
# 																					publish-stack: package-stack
# 																						ls -lrt
# 																							@echo "Publishing Package : ${TAR_FILE} to Artifactory: https://artifacts.wellmanage.com/artifactory/${ARTIFACT_GROUP}/${STACK}"
# 																								curl -i -k -u "${ARTIFACT_ACCESS_KEY}" -T $(TAR_FILE) "https://artifacts.wellmanage.com/artifactory/${ARTIFACT_GROUP}/${STACK}/${STACK}-${BUILD_VERSION}.tgz"
#
# 																								aws-deploy-stack: guard-STACK guard-ENV publish-stack
# 																									@echo "Doing AWS Deployment for Stack: ${STACK}. VPC:${VPC}, Environment: ${ENV}"
# 																										
# 																											@if [ "${ENV}" != "prod" ]; then \
# 																													@echo "Environment is PROD not going to execute AWS Deploy "; \
# 																															awsWmcDeploy ${VPC} ${STACK} "https://artifacts.wellmanage.com/artifactory/${ARTIFACT_GROUP}/${STACK}/${STACK}-${BUILD_VERSION}.tgz"
#
# 																															build-docker:
# 																																ls -lrt
# 																																	docker build -t jets/appointment-service --build-arg APP_ENV=prod --build-arg APP_NAME=appointment-service --build-arg DEPLOY_ARTIFACT_DIR=target --build-arg SERVICE_PORT=8080 --build-arg ARTIFACT_NAME=appointment-service-0.0.1-SNAPSHOT.jar --build-arg --no-cache . 
#
# 																																	test-docker:
# 																																		// docker run --rm -v `pwd`/test:`pwd`/test ${BUILD_IMAGE_PATH} /usr/local/bin/goss validate -f junit > `pwd`/test/results.xml
# 																																			@echo "Tests go here"
# 																																				
