using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Entityes
{
    [Table("Places-Types")]
    public class PlaceType
    {
        public PlaceType()
        {
            this.Id = 0;
            this.Name = string.Empty;
        }


        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }
    }
}
