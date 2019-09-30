FROM maven:3.5.3-jdk-8-slim as builder
MAINTAINER Sapient

ARG APP_ENV
ARG APP_NAME
ARG DEPLOY_ARTIFACT_DIR
ARG ARTIFACT_NAME
ARG JENKINS_REMOTING_VERSION=3.27
ARG SERVICE_PORT=8090

# create runtime user and group "${APP_NAME}"

RUN addgroup --system ${APP_NAME} --gid 1005 &&\
 adduser -u 1005 --system ${APP_NAME} --ingroup ${APP_NAME} --home /home/${APP_NAME} --shell /bin/bash &&\
 chmod 755 /home/${APP_NAME}

#Creating Application Directory and Giving Appropriate Permissions

RUN mkdir -p /apps/${APP_NAME} &&\
        chown -R ${APP_NAME}:${APP_NAME} /apps/${APP_NAME} &&\
        chmod -R 755 /apps/${APP_NAME}

#Creating Logs Directory and Giving Appropriate Permissions

RUN mkdir -p /logs/${APP_NAME} && \
        chown -R ${APP_NAME}:${APP_NAME} /logs/${APP_NAME} && \
        chmod -R 755 /logs/${APP_NAME}

# Copy Deploy Artifacts

WORKDIR /apps/${APP_NAME}
COPY ./startup.sh .
COPY ./pom.xml .
COPY ${DEPLOY_ARTIFACT_DIR}/${ARTIFACT_NAME} /apps/${APP_NAME}/app.jar
COPY ./ZAP .
COPY URLs.txt .
COPY ./drivers/linux/chromium-codecs-ffmpeg_74.0.3729.169-0ubuntu0.16.04.1_amd64.deb .
COPY ./drivers/linux/chromium-codecs-ffmpeg-extra_74.0.3729.169-0ubuntu0.16.04.1_amd64.deb .
COPY ./drivers/linux/chromium-browser_74.0.3729.169-0ubuntu0.16.04.1_amd64.deb .

RUN touch /home/${APP_NAME}/.bashrc \
        && echo export ENV=${APP_ENV} >> /home/${APP_NAME}/.bashrc \
        && echo export APP_ENV=${APP_ENV} >> /home/${APP_NAME}/.bashrc
RUN sed -i -e '/^assistive_technologies=/s/^/#/' /etc/java-8-openjdk/accessibility.properties

# Slave Configuration

RUN curl --create-dirs -sSLo /usr/share/jenkins/slave.jar https://repo.jenkins-ci.org/public/org/jenkins-ci/main/remoting/$JENKINS_REMOTING_VERSION/remoting-$JENKINS_REMOTING_VERSION.jar \
  && chmod 755 /usr/share/jenkins \
  && chmod 644 /usr/share/jenkins/slave.jar

COPY jenkins-slave /usr/local/bin/jenkins-slave
RUN chmod a+x /usr/local/bin/jenkins-slave
RUN apt-get update -y

# Adding Required Packages for Chrome Browser

RUN apt-get install libglib2.0 wget gnupg fonts-liberation libappindicator3-1 libgtk-3-0 libxss1 xdg-utils unzip xvfb libxi6 libgconf-2-4 libnss3 -y

RUN curl -sS -o - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add && \
    echo "deb [arch=amd64]  http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list && \
    apt-get install libasound2 libatomic1 -y && \
    dpkg -i /apps/${APP_NAME}/chromium-codecs-ffmpeg_74.0.3729.169-0ubuntu0.16.04.1_amd64.deb && \
    dpkg -i /apps/${APP_NAME}/chromium-codecs-ffmpeg-extra_74.0.3729.169-0ubuntu0.16.04.1_amd64.deb && \
    dpkg -i /apps/${APP_NAME}/chromium-browser_74.0.3729.169-0ubuntu0.16.04.1_amd64.deb

# setup Git and Git LFS
RUN build_deps="curl" && \
        apt-get update && \
        DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends ${build_deps} ca-certificates && \
        curl -s https://packagecloud.io/install/repositories/github/git-lfs/script.deb.sh | bash && \
        DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends git-lfs && \
        git lfs install --skip-smudge && \
        DEBIAN_FRONTEND=noninteractive apt-get purge -y --auto-remove ${build_deps} && \
        rm -r /var/lib/apt/lists/*

RUN chmod -R 777 /apps/${APP_NAME}
USER ${APP_NAME}
ENV APP_NAME=${APP_NAME}
ENV ENV=${APP_ENV}
ENV APP_ENV=${APP_ENV}
EXPOSE ${SERVICE_PORT}/tcp
EXPOSE ${SERVICE_PORT}/udp

CMD sh /apps/${APP_NAME}/startup.sh
