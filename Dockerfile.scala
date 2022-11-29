FROM sbtscala/scala-sbt:openjdk-11.0.16_1.8.0_3.2.1
ADD ./ /home/
WORKDIR /home/practica_big_data_2019
RUN apt-get update
RUN apt-get install pip -y
RUN pip install -r requirements.txt
RUN pip install joblib
RUN ./resources/download_data.sh
WORKDIR /home/practica_big_data_2019/flight_prediction


