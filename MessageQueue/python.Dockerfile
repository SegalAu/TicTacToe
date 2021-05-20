FROM python:3.8

RUN pip3 install pika==1.1.0
RUN pip3 install py4j
#RUN pip3 install pyzmq==19.0.1

WORKDIR /app
COPY . .

COPY wait.sh /wait.sh


# for ZeroMQ server
#EXPOSE 5555

CMD /wait.sh rabbitmq 5672 \ 
&& python3 receive.py
