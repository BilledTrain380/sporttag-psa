FROM node:lts-alpine
LABEL "com.azure.dev.pipelines.agent.handler.node.path"="/usr/local/bin/node"

COPY apt-sources.list /etc/apt/sources.list
COPY google-chrome-linux-key.pub /root
RUN \
    apk add bash sudo shadow && \
    apt-key add /root/google-chrome-linux-key.pub && \
    rm -f /root/google-chrome-linux-key.pub && \
    apt-get update && \
    apt-get install -y google-chrome-stable xvfb && \
    rm -rf /var/lib/apt/lists/*