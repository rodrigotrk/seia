#!/bin/bash

HOST=$1
USER=$2
PASSWORD=$3
DATABASE=$4
OUTPUT_PATH=$5
IDE_LOCALIZACAO_GEOGRAFICA=$6

APP_PATH='/usr/lib/postgresql/8.4/bin'

TIME=`date +%H%M%S`

QUERY="CREATE VIEW sinaflor_shape_$TIME AS SELECT the_geom FROM dado_geografico where ide_localizacao_geografica in ($IDE_LOCALIZACAO_GEOGRAFICA);"

mkdir -p /tmp/$IDE_LOCALIZACAO_GEOGRAFICA
echo $QUERY > /tmp/$IDE_LOCALIZACAO_GEOGRAFICA/create_sinaflor_shape.sql  

export PGPASSWORD=$PASSWORD

CMD="psql -U $USER -h $HOST -d $DATABASE -f /tmp/$IDE_LOCALIZACAO_GEOGRAFICA/create_sinaflor_shape.sql"
$CMD

CMD="$APP_PATH/pgsql2shp -f $OUTPUT_PATH -h $HOST -u $USER -P $PASSWORD $DATABASE sinaflor_shape_$TIME"
$CMD

QUERY="DROP VIEW sinaflor_shape_$TIME;"
echo $QUERY > /tmp/$IDE_LOCALIZACAO_GEOGRAFICA/drop_sinaflor_shape.sql  

CMD="psql -U $USER -h $HOST -d $DATABASE -f /tmp/$IDE_LOCALIZACAO_GEOGRAFICA/drop_sinaflor_shape.sql"
$CMD
