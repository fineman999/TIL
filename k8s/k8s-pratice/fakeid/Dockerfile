FROM ubuntu:18.04
RUN apt-get update && apt-get install -y rig boxes zsh
ENV INTERVAL 5
ENV OPTION stone
ADD genid.sh /bin/genid.sh
RUN chmod +x /bin/genid.sh
ENTRYPOINT ["/bin/zsh", "/bin/genid.sh"]