using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Photos")]
    public class Photo
    {
        public Photo()
        {
            this.Id = 0;
            this.Link = string.Empty;
        }


        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("link")]
        public string Link { get; set; }
    }
}
