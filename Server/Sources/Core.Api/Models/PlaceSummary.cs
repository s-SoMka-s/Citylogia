using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;
using System.Linq;

namespace Core.Api.Models
{
    public class PlaceSummary
    {
        public PlaceSummary(Place source)
        {
            this.Id = source.Id;
            this.Mark = source.Mark;
            this.Name = source.Name;
            this.Description = source.Description;
            this.Type = source.Type;
            this.Address = source.Address;

            if (source.Photos != default)
            {
                this.Photos = new BaseCollectionResponse<Photo>(source.Photos.ToList());
            }

            if (source.Reviews != default)
            {
                this.Reviews = new BaseCollectionResponse<Review>(source.Reviews.ToList());
            }
        }

        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("mark")]
        public long Mark { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("description")]
        public string Description { get; set; }


        [JsonProperty("type")]
        public PlaceType Type { get; set; }

        [JsonProperty("address")]
        public Address Address { get; set; }

        [JsonProperty("photo")]
        public BaseCollectionResponse<Photo> Photos { get; set; }

        [JsonProperty("reviews")]
        public BaseCollectionResponse<Review> Reviews { get; set; }
    }


}
