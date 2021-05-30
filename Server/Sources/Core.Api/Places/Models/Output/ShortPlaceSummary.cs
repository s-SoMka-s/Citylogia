using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;
using System.Linq;

namespace Core.Api.Places.Models.Output
{
    public class ShortPlaceSummary
    {
        public ShortPlaceSummary(Place source, bool isFavorite = false)
        {
            this.Id = source.Id;
            this.Name = source.Name;
            this.Mark = source.Mark;

            this.IsFavorite = isFavorite;

            this.MainPhoto = source.Photos
                                   .Where(p => p.IsMain)
                                   .Select(p => new FileSummary(p.Photo))
                                   .FirstOrDefault();

            this.Type = new PlaceTypeSummary(source.Type);

            this.Latitude = source.Latitude;
            this.Longtitude = source.Longitude;
            this.City = source.City;
            this.Street = source.Street;
            this.House = source.House;
        }

        [JsonProperty("id")]
        public long Id { get;}

        [JsonProperty("name")]
        public string Name { get;}

        [JsonProperty("mark")]
        public long Mark { get; }

        [JsonProperty("is_favorite")]
        public bool IsFavorite { get; }

        [JsonProperty("main_photo")]
        public FileSummary MainPhoto { get; }

        [JsonProperty("type")]
        public PlaceTypeSummary Type { get;}

        [JsonProperty("latitude")]
        public double Latitude { get; }

        [JsonProperty("longitude")]
        public double Longtitude { get; }

        [JsonProperty("city")]
        public string City { get; }

        [JsonProperty("street")]
        public string Street { get; }

        [JsonProperty("house")]
        public long House { get; }
    }

}
