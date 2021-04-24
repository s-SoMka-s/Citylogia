using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;

namespace Core.Api
{
    public class ReviewInputParameters
    {
        [Required]
        [JsonProperty("mark")]
        public long Mark { get; set; }

        [Required]
        [JsonProperty("body")]
        public string Body { get; set; }

        [Required]
        [JsonProperty("user_id")]
        public long UserId { get; set; }


        public Review Build()
        {
            return new Review()
            {
                Mark = Mark,
                Body = Body,
                UserId = UserId
            };
        }
    }
}