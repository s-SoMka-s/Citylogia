using Newtonsoft.Json;

namespace Core.Api.Places.Models.Input
{
    public class PlaceUpdateParameters
    {

        [JsonProperty("is_approved")]
        public bool IsApproved { get; set; }

    }
}
