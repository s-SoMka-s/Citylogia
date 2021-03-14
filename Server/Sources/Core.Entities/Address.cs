using Libraries.Db.Base;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Address")]
    public class Address : BaseDataType
    {
        public Address() : base()
        {
            this.Latitude = 0d;
            this.Longitude = 0d;
            this.Country = string.Empty;
            this.Province = string.Empty;
            this.City = string.Empty;
            this.District = string.Empty;
            this.House = 0;
            this.Flat = null;
            this.Postcode = 0;
        }

        public double Latitude { get; set; }

        public double Longitude { get; set; }

        public string Country { get; set; }

        public string Province { get; set; }

        public string City { get; set; }

        public string District { get; set; }

        public string Street { get; set; }

        public long House { get; set; }

        public long? Flat { get; set; }

        public long Postcode { get; set; } 
    }
}
