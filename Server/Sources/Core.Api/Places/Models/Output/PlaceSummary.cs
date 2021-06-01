using Citylogia.Server.Core.Entityes;
using Core.Api.Models;
using Newtonsoft.Json;
using System.Collections.Generic;
using System.Linq;

namespace Core.Api.Places.Models.Output
{
    public class PlaceSummary
    {
        public PlaceSummary(Place source, HashSet<long> favorites)
        {
            this.Id = source.Id;
            this.Mark = source.Mark;
            this.IsFavorite = favorites.Contains(this.Id);
            this.Name = source.Name;
            this.ShortDescription = source.ShortDescription;

            this.Description = source.Description;
            this.Type = new PlaceTypeSummary(source.Type);

            this.City = source.City;
            this.Street = source.Street;
            this.House = source.House;
            this.Latitude = source.Latitude;
            this.Longtitude = source.Longitude;

            if (source.Photos != default)
            {
                var photos = source.Photos.Select(p => new FileSummary(p.Photo)).ToList();
                this.Photos = new BaseCollectionResponse<FileSummary>(photos);
            }

            if (source.Reviews != default)
            {
                var reviews = source.Reviews.Select(r => new ReviewSummary(r)).ToList();
                this.Reviews = new BaseCollectionResponse<ReviewSummary>(reviews);
            }
        }


        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("mark")]
        public long Mark { get; set; }

        [JsonProperty("is_favorite")]
        public bool IsFavorite { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("short_description")]
        public string ShortDescription { get; set; }

        [JsonProperty("description")]
        public string Description { get; set; }

        [JsonProperty("type")]
        public PlaceTypeSummary Type { get; set; }
        
        [JsonProperty("city")]
        public string City { get; }

        [JsonProperty("street")]
        public string Street { get; }

        [JsonProperty("house")]
        public long House { get; }

        [JsonProperty("latitude")]
        public double Latitude { get; set; }

        [JsonProperty("longitude")]
        public double Longtitude { get; set; }

        [JsonProperty("photos")]
        public BaseCollectionResponse<FileSummary> Photos { get; set; }

        [JsonProperty("reviews")]
        public BaseCollectionResponse<ReviewSummary> Reviews { get; set; }
    }
}
