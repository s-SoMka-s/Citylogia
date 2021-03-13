using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Photos")]
    public class Photo
    {
        public Photo()
        {
            this.Link = string.Empty;
        }

        [Key]
        public long Id { get; set; }
        public string Link { get; set; }
    }
}
