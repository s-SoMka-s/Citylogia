using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System;

namespace Core.Api.Places.Models.Input
{
    public class PlaceSelectParameters
    {
        [FromQuery(Name = "latitude")]
        public double Latitude { get; set; }

        [FromQuery(Name = "longitude")]
        public double Longtitude { get; set; }

        [FromQuery(Name = "radius_in_km")]
        public double RadiusInKm { get; set; }

        [FromQuery(Name = "type_ids")]
        public long[] TypeIds { get; set; } = Array.Empty<long>();
    }
}
