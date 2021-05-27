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
            this.IsApproved = false;
            this.Author = default;
            this.Mark = 0;
            this.Name = string.Empty;
            this.ShortDescription = string.Empty;
            this.Description = string.Empty;
            this.Type = default;
            this.Longitude = default;
            this.Latitude = default;
            this.City = "Новосибирск";
            this.Street = default;
            this.House = 0;
            this.Photos = default;
        }

        public bool IsApproved { get; set; }

        // тот кто предложил место
        [ForeignKey(nameof(User))]
        public long UserId { get; set; }
        public User Author { get; set; }

        public long Mark { get; set; }
        public string Name { get; set; }
        public string ShortDescription { get; set; }
        public string Description { get; set; }

        [ForeignKey(nameof(PlaceType))]
        public long TypeId { get; set; }

        public PlaceType Type { get; set; }

        public double Latitude { get; set; }

        public double Longitude { get; set; }

        public string City { get; set; }
        public string Street { get; set; }
        public long House { get; set; }

        public virtual ICollection<Photo> Photos { get; set; }

        public virtual ICollection<Review> Reviews { get; set; }
    }
}
