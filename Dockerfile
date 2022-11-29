FROM bitnami/spark:3.1.2 
ADD ./ /home/
WORKDIR /home/practica_big_data_2019
USER root
RUN pip install -r requirements.txt
RUN pip install --user joblib
RUN ./resources/download_data.sh
ENV SPARK_HOME=/opt/bitnami/spark
