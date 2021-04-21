using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;

namespace Core.Api.Places.Models.Input
{
    public class NewPlaceTypeParameters
    {
        [JsonProperty("name")]
        public string Name { get; set; }

        public PlaceType Buid()
        {
            return new PlaceType()
            {
                Name = Name
            };
        }
    }
}
