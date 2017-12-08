FROM redis
ADD sentinel.conf /usr/local/etc/redis/sentinel.conf
RUN chown redis:redis /usr/local/etc/redis/sentinel.conf
CMD ["redis-server", "/usr/local/etc/redis/sentinel.conf", "--sentinel"]