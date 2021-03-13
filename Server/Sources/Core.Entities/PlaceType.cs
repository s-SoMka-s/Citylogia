using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Places-Types")]
    public class PlaceType
    {
        public PlaceType()
        {
            this.Name = string.Empty;
        }

        [Key]
        public long Id { get; set; }
        public string Name { get; set; }
    }
}
