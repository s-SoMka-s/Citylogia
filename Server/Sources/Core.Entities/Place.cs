using Libraries.Db.Base;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Places")]
    public class Place : BaseDataType
    {
        public Place() : base()
        {
            this.Mark = 0;
            this.Name = string.Empty;
            this.ShortDescription = string.Empty;
            this.Description = string.Empty;
            this.Type = default;
            this.Longitude = default;
            this.Latitude = default;
            this.Address = default;
            this.Photos = default;
        }


        public long Mark { get; set; }

        public string Name { get; set; }

        public string ShortDescription { get; set; }
        
        public string Description { get; set; }

        [ForeignKey(nameof(PlaceType))]
        public long TypeId { get; set; }

        public PlaceType Type { get; set; }

        public double Latitude { get; set; }

        public double Longitude { get; set; }

        public string Address { get; set; }

        public Photo[] Photos { get; set; }

        public virtual ICollection<Review> Reviews { get; set; }
    }
}
