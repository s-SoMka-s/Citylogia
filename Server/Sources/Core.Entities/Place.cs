using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Places")]
    public class Place
    {
        public Place()
        {
            this.Mark = 0;
            this.Name = string.Empty;
            this.Description = string.Empty;
            this.Type = default;
            this.Address = default;
            this.Photos = default;
            this.Reviews = default;
        }

        [Key]
        public long Id { get; set; }

        public long Mark { get; set; }

        public string Name { get; set; }
        
        public string Description { get; set; }


        [ForeignKey(nameof(PlaceType))]
        public long TypeId { get; set; }

        public PlaceType Type { get; set; }

        [ForeignKey(nameof(Address))]
        public long AddressId { get; set; }
        public Address Address { get; set; }

        public Photo[] Photos { get; set; }

        public Review[] Reviews { get; set; }
    }
}
