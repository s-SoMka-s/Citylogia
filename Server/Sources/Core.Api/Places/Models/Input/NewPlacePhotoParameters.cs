using Newtonsoft.Json;

namespace Core.Api.Places.Models.Input
{
    public class NewPlacePhotoParameters : NewFileParameters
    {
        [JsonProperty("is_main")]
        public bool IsMain { get; set; }
    }
}
