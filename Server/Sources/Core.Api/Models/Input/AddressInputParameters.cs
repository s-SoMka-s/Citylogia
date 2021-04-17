using Newtonsoft.Json;


namespace Core.Api.Models.Input
{
    public class AddressInputParameters
    {
        [JsonProperty("latitude")]
        public double Latitude { get; set; }

        [JsonProperty("longitude")]
        public double Longitude { get; set; }

        [JsonProperty("country")]
        public string Country { get; set; }

        [JsonProperty("province")]
        public string Province { get; set; }

        [JsonProperty("city")]
        public string City { get; set; }

        [JsonProperty("district")]
        public string District { get; set; }

        [JsonProperty("street")]
        public string Street { get; set; }

        [JsonProperty("house")]
        public long House { get; set; }

        [JsonProperty("flat")]
        public long? Flat { get; set; }

        [JsonProperty("postcode")]
        public long Postcode { get; set; }
    }
}
