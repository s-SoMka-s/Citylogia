using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Places")]
    public class Place
    {
        public Place()
        {
            this.Id = 0;
            this.Mark = 0;
            this.Name = string.Empty;
            this.Description = string.Empty;
            this.Type = default;
            this.Address = default;
            this.Photos = default;
            this.Reviews = default;
        }

        [JsonProperty("id")]
        public long Id { get; set; }    

        [JsonProperty("mark")]
        public long Mark { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }
        
        [JsonProperty("description")]
        public string Description { get; set; }


        [ForeignKey(nameof(PlaceType))]
        public long TypeId { get; set; }

        [JsonProperty("type")]
        public PlaceType Type { get; set; }

        [ForeignKey(nameof(Address))]
        public long AddressId { get; set; }
        [JsonProperty("address")]
        public Address Address { get; set; }

        [JsonProperty("photo")]
        public Photo[] Photos { get; set; }

        [JsonProperty("reviews")]
        public Review[] Reviews { get; set; }
    }
}
