using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;

namespace Core.Api
{
    public class ReviewInputParameters
    {
        [JsonProperty("mark")]
        public long Mark { get; set; }

        [JsonProperty("body")]
        public string Body { get; set; }

        [JsonProperty("user_id")]
        public long UserId { get; set; }


        public Review Build(long placeId)
        {
            return new Review()
            {
                Mark = Mark,
                Body = Body,
                UserId = UserId,
                PlaceId = placeId
            };
        }
    }
}