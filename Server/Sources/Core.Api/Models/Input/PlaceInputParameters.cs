using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;

namespace Core.Api.Models.Input
{
    public class PlaceInputParameters
    {
        [JsonProperty("name")]
        public string Name { get; set; }
        
        [JsonProperty("mark")]
        public long Mark { get; set; }

        [JsonProperty("description")]
        public string Description { get; set; }

        [JsonProperty("type_id")]
        public long TypeId { get; set; }

        [JsonProperty("address")]
        public AddressInputParameters Address { get; set; }

        public Address BuildAddress()
        {
            return new Address()
            {
                Latitude = Address.Latitude,
                Longitude = Address.Longitude,
                Country = Address.Country,
                Province = Address.Province,
                District = Address.District,
                Street = Address.Street,
                House = Address.House,
                Flat = Address.Flat,
                Postcode = Address.Postcode
            };
        }

        public Place BuildPlace(long addressId)
        {
            return new Place()
            {
                Mark = Mark,
                Name = Name,
                Description = Description,
                TypeId = TypeId,
                AddressId = addressId
            };
        }

    }
}
