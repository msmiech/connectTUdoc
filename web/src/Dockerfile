FROM httpd:2.4-alpine
COPY ./dist/frontend/ /usr/local/apache2/htdocs/
#RUN mkdir -p /usr/local/apache2/htdocs/node_modules/
#COPY ./node_modules/ /usr/local/apache2/htdocs/node_modules/
COPY ./apache/.htaccess /usr/local/apache2/htdocs/
COPY ./apache/httpd.config /usr/local/apache2/conf/httpd.conf
