#################################
#      Flight Data Access       #
#################################
# To clear data
curl -X DELETE "http://localhost:8080/api/clear" \
  -H "accept: */*" \
  -H "Content-Type: application/json"

# To remove data by id
curl -X DELETE "http://localhost:8080/api/data/0" \
  -H "accept: */*" \
  -H "Content-Type: application/json"

# To add one data
curl -X POST "http://localhost:8080/api/data/add" \
  -H "accept: */*" \
  -H "Content-Type: application/json" \
  -d "{\"flight\":[{\"date\":\"12/05/2021\",
      \"iataStart\":\"MUC\",
      \"iataDest\":\"BER\",
      \"class\":\"business\",
      \"authorityNumber\":123456,
      \"travelling\":1},
      {\"date\":\"12/04/2021\",
      \"iataStart\":\"DUS\",
      \"iataDest\":\"LAX\",
      \"class\":\"economy\",
      \"authorityNumber\":678910,
      \"travelling\":3},
      {\"date\":\"09/06/2021\",
      \"iataStart\":\"UTK\",
      \"iataDest\":\"BER\",
      \"class\":\"business\",
      \"authorityNumber\":1,
      \"travelling\":1}
      ]}"


#################################
#      Input Data Access        #
#################################
# To create temporary database
curl -X POST "http://localhost:8080/api/input/create" \
  -H "accept: */*" \
  -H "Content-Type: application/json"

# To remove data by id
curl -X DELETE "http://localhost:8080/api/input/data/1" \
  -H "accept: */*" \
  -H "Content-Type: application/json"

# To add data
curl -X POST "http://localhost:8080/api/input/data/add" \
  -H "accept: */*" \
  -H "Content-Type: application/json" \
  -d "{\"flight\":[{\"date\":\"13/05/2021\",
      \"iataStart\":\"MUC\",
      \"iataDest\":\"BER\",
      \"class\":\"business\",
      \"authorityNumber\":123456,
      \"travelling\":1},
      {\"date\":\"14/05/2021\",
      \"iataStart\":\"DUS\",
      \"iataDest\":\"LAX\",
      \"class\":\"economy\",
      \"authorityNumber\":123456,
      \"travelling\":5}
      ]}"

# To update data
curl -X POST "http://localhost:8080/api/input/data" \
  -H "accept: */*" \
  -H "Content-Type: application/json" \
  -d "{\"flight\":[{\"update\":[
      {\"date\":\"12/03/2021\",
      \"iataStart\":\"DUS\",
      \"iataDest\":\"LAX\",
      \"class\":\"economy\",
      \"authorityNumber\":123456},
      {\"date\":\"12/03/2021\",
      \"iataStart\":\"BER\",
      \"iataDest\":\"LAX\",
      \"class\":\"economy\",
      \"authorityNumber\":123456,
      \"travelling\":3}
      ]}]}"

# To merge data
curl -X DELETE "http://localhost:8080/api/input/merge" \
  -H "accept: */*" \
  -H "Content-Type: application/json"

curl -X POST "http://localhost:8080/api/room/book/E2014" -H "accept: */*" -H "Content-Type: application/json" -d "{\"date\":\"10.12.2022\", \"startTime\": \"12:00\", \"endTime\":\"15:00\", \"seats\":2}"




