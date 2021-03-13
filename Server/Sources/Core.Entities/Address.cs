using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Address")]
    public class Address
    {
        public Address()
        {
            this.Id = 0;
            this.Latitude = 0d;
            this.Longitude = 0d;
            this.Country = string.Empty;
            this.Province = string.Empty;
            this.City = string.Empty;
            this.District = string.Empty;
            this.House = 0;
            this.Flat = null;
            this.Postcode = 0;
        }

        public long Id { get; set; }

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
