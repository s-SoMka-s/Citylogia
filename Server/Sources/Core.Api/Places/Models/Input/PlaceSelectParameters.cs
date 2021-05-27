using Microsoft.AspNetCore.Mvc;
using System;

namespace Core.Api.Places.Models.Input
{
    public class PlaceSelectParameters
    {
        [FromQuery(Name = "type_ids")]
        public long[] TypeIds { get; set; } = Array.Empty<long>();
    }
}
